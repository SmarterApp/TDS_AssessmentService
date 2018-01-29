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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tds.assessment.repositories.loader.AffinityGroupItemRepository;
import tds.assessment.repositories.loader.AffinityGroupRepository;
import tds.assessment.services.AffinityGroupLoaderService;

@RunWith(MockitoJUnitRunner.class)
public class AffinityGroupLoaderServiceImplTest extends AssessmentLoaderServiceBaseTest {
    private AffinityGroupLoaderService service;

    @Mock
    private AffinityGroupRepository affinityGroupRepository;

    @Mock
    private AffinityGroupItemRepository affinityGroupItemRepository;

    @Before
    public void setup() {
        service = new AffinityGroupLoaderServiceImpl(affinityGroupRepository, affinityGroupItemRepository);
    }

    @Test
    public void shouldLoadAffinityGroup() {
//        service.loadAffinityGroups(mockTestPackage, );
    }

}
