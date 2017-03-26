package tds.assessment.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import tds.assessment.ItemFileMetadata;
import tds.assessment.services.ItemService;
import tds.common.web.exceptions.NotFoundException;

@RestController
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "assessments/item/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ItemFileMetadata> findItemFileMetadata(@RequestParam final String clientName,
                                                          @RequestParam final long bankKey,
                                                          @RequestParam(required = false) final Long stimulusKey,
                                                          @RequestParam(required = false) final Long itemKey) {
        if (stimulusKey == null && itemKey == null) {
            throw new IllegalArgumentException("Item lookup requires stimulus key or item key");
        }

        Optional<ItemFileMetadata> maybeItemFile;

        if (stimulusKey != null) {
            maybeItemFile = itemService.findItemFileMetadataByStimulusKey(clientName, bankKey, stimulusKey);
        } else {
            maybeItemFile = itemService.findItemFileMetadataByItemKey(clientName, bankKey, itemKey);
        }

        if (!maybeItemFile.isPresent()) {
            throw new NotFoundException("Could not find item file metadata");
        }

        return ResponseEntity.ok(maybeItemFile.get());
    }
}
