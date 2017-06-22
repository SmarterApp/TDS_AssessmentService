/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

package tds.assessment.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import tds.assessment.AssessmentWindow;
import tds.assessment.model.AssessmentFormWindowProperties;
import tds.assessment.model.AssessmentWindowParameters;
import tds.assessment.repositories.AssessmentWindowQueryRepository;
import tds.assessment.services.AssessmentWindowService;
import tds.common.cache.CacheType;

@Service
public class AssessmentWindowServiceImpl implements AssessmentWindowService {
    private final AssessmentWindowQueryRepository assessmentWindowQueryRepository;

    @Autowired
    public AssessmentWindowServiceImpl(final AssessmentWindowQueryRepository assessmentWindowQueryRepository) {
        this.assessmentWindowQueryRepository = assessmentWindowQueryRepository;
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public Map<String, List<AssessmentWindow>> findAssessmentWindowsForAssessmentIds(final String clientName, final String... assessmentIds) {
        List<AssessmentWindow> assessmentWindows = assessmentWindowQueryRepository.findAssessmentWindowsForAssessmentIds(clientName, assessmentIds);
        return assessmentWindows.stream()
            .collect(Collectors.groupingBy(AssessmentWindow::getAssessmentKey));
    }

    @Override
    @Cacheable(CacheType.LONG_TERM)
    public List<AssessmentWindow> findAssessmentWindows(final AssessmentWindowParameters assessmentWindowParameters) {
        boolean guestStudent = assessmentWindowParameters.isGuestStudent();
        String clientName = assessmentWindowParameters.getClientName();
        String assessmentId = assessmentWindowParameters.getAssessmentId();

        //Lines StudentDLL 5955 - 5975
        List<AssessmentWindow> examFormWindows = assessmentWindowQueryRepository.findCurrentAssessmentFormWindows(clientName,
            assessmentId,
            assessmentWindowParameters.getShiftWindowStart(),
            assessmentWindowParameters.getShiftWindowEnd(),
            assessmentWindowParameters.getShiftFormStart(),
            assessmentWindowParameters.getShiftFormEnd()
        );

        if (!examFormWindows.isEmpty()) {
            //Logic in StudentDLL lines 5963
            //The first call in the StudentDLL._GetTesteeTestForms_SP is to get the window for the guest which is duplication from earlier code
            return findCurrentExamWindowFromFormWindows(assessmentWindowParameters, examFormWindows);
        }

        //Lines 5871-5880 StudentDLL._GetTestteeTestWindows_SP()
        List<AssessmentWindow> assessmentWindows = assessmentWindowQueryRepository.findCurrentAssessmentWindows(clientName,
            assessmentWindowParameters.getShiftWindowStart(),
            assessmentWindowParameters.getShiftWindowEnd(),
            assessmentId);

        if (guestStudent) {
            return assessmentWindows;
        }

        //Lines 6001-6011 in StudentDLL._GetTesteeTestWindows_SP
        return assessmentWindows.stream()
            .filter(distinctByKey(AssessmentWindow::getWindowId))
            .collect(Collectors.toList());
    }

    private List<AssessmentWindow> findCurrentExamWindowFromFormWindows(final AssessmentWindowParameters assessmentWindowParameters, final List<AssessmentWindow> formWindows) {
        boolean requireFormWindow = false, requireForm = false;

        //Lines 3703 - 3710 in StudentDLL._GetTesteeTestForms_SP
        if (assessmentWindowParameters.isGuestStudent()) {
            return formWindows;
        }

        //Lines 3712 - 3730 in StudentDLL._GetTesteeTestForms_SP
        //TODO - revisit this because it isn't currently being used
        Optional<AssessmentFormWindowProperties> maybeAssessmentProperties = assessmentWindowQueryRepository.findAssessmentFormWindowProperties(assessmentWindowParameters.getClientName(), assessmentWindowParameters.getAssessmentId());
        if (maybeAssessmentProperties.isPresent()) {
            AssessmentFormWindowProperties properties = maybeAssessmentProperties.get();
        }

        String formList = assessmentWindowParameters.getFormList();
        if (formList != null) {
            if (formList.indexOf(':') > -1)
                requireFormWindow = true;
            else {
                requireForm = true;
                requireFormWindow = false;
            }
        }

        //Key is a the form key and the value is the list of window ids associated with the form key
        Map<String, Set<String>> studentPackageForms = new HashMap<>();

        if (formList != null) {
            //Lines 3753 - 3781 in StudentDLL._GetTesteeTestForms_SP
            String[] forms = formList.split(";");
            for (String formValue : forms) {
                String wid;
                String form;

                int idx;
                if ((idx = formValue.indexOf(":")) > -1) {// i.e. found
                    wid = formValue.substring(0, idx);
                    form = formValue.substring(idx + 1);
                } else {
                    form = formValue;
                    wid = null;
                }

                if (!studentPackageForms.containsKey(form)) {
                    studentPackageForms.put(form, new HashSet<>());
                }

                if (wid != null) {
                    studentPackageForms.get(form).add(wid);
                }
            }
        }

        //Lines 3786 - 3815 in StudentDLL._GetTesteeTestForms_SP
        if (requireFormWindow) {
            return formWindows.stream().filter(assessmentWindow -> {
                String formKey = assessmentWindow.getFormKey();
                return studentPackageForms.containsKey(formKey) &&
                    studentPackageForms.get(formKey).contains(assessmentWindow.getWindowId());
            }).filter(distinctByKey(AssessmentWindow::getWindowId)).collect(Collectors.toList());
        } else if (requireForm) {
            return formWindows.stream().filter(assessmentWindow ->
                studentPackageForms.containsKey(assessmentWindow.getFormKey()))
                .filter(distinctByKey(AssessmentWindow::getWindowId))
                .collect(Collectors.toList());
        }

        return formWindows;
    }

    private static <T> Predicate<T> distinctByKey(final Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
