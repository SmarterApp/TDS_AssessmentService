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

package tds.assessment.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

import tds.assessment.model.TestToolDefaults;

public interface TestToolDefaultsHelper {


    Map<String, String> TOOL_OPTION_DEFAULTS_MAP = new ImmutableMap.Builder<String, String>()
        // ASL
        .put("TDS_ASL0", "Do not show ASL videos")
        .put("TDS_ASL1", "Show ASL videos")
        // Audio Playback Controls
        .put("TDS_APC_PSP", "Play Stop and Pause")
        .put("TDS_APC_SCRUBBER", "Scrubber")
        // Braille Type
        .put("TDS_BT_ECN", "EBAE - Contracted - Nemeth Math")
        .put("TDS_BT_ECT", "EBAE - Contracted - UEB Math")
        .put("TDS_BT_EXN", "EBAE - Uncontracted - Nemeth Math")
        .put("TDS_BT_EXT", "EBAE - Uncontracted - UEB Math")
        .put("TDS_BT_G1", "Uncontracted")
        .put("TDS_BT_G2", "Contracted")
        .put("TDS_BT_NM", "Nemeth")
        .put("TDS_BT_UCN", "UEB - Contracted - Nemeth Math")
        .put("TDS_BT_UCT", "UEB - Contracted - UEB Math")
        .put("TDS_BT_UXN", "UEB - Uncontracted - Nemeth Math")
        .put("TDS_BT_UXT", "UEB - Uncontracted - UEB Math")
        .put("TDS_BT0", "Not Applicable")
        // Calculator
        .put("TDS_Calc0", "None")
        .put("TDS_CalcBasic", "Basic")
        .put("TDS_CalcSci", "Scientific")
        .put("TDS_CalcSci&TDS_CalcGraphing&TDS_CalcRegress", "Scientific&Graphing&Regressions")
        .put("TDS_CalcSciInv", "ScientificInv")
        .put("TDS_CalcSciInv&TDS_CalcGraphingInv&TDS_CalcRegress", "ScientificInv&GraphingInv&Regressions")
        // Closed Captioning
        .put("TDS_ClosedCap0", "Closed Captioning Not Available")
        .put("TDS_ClosedCap1", "Closed Captioning Available")
        // Color Choices
        .put("TDS_CC0", "Black on White")
        .put("TDS_CCInvert", "Reverse Contrast")
        .put("TDS_CCMagenta", "Black on Rose")
        .put("TDS_CCMedGrayLtGray", "Medium Gray on Light Gray")
        .put("TDS_CCYellowB", "Yellow on Blue")
        // Dictionary
        .put("TDS_Dict0", "None")
        .put("TDS_Dict_SD2", "MerriamWebsters Elementary Dictionary")
        .put("TDS_Dict_SD3", "MerriamWebsters Intermediate Dictionary")
        .put("TDS_Dict_SD4", "MerriamWebsters School Dictionary")
        // Emboss
        .put("TDS_Emboss0", "None")
        .put("TDS_Emboss_Stim&TDS_Emboss_Item", "Stimuli&Items")
        // Emboss Request Type
        .put("TDS_ERT0", "Not Applicable")
        .put("TDS_ERT_OR&TDS_ERT_Auto", "Auto-Request")
        .put("TDS_ERT_OR", "On-Request")
        // Expandable Passages
        .put("TDS_ExpandablePassages0", "Expandable Passages Off")
        .put("TDS_ExpandablePassages1", "Expandable Passages On")
        // Font Type
        .put("TDS_FT_Serif", "Time New Roman")
        .put("TDS_FT_Verdana", "Verdana")
        // Global Notes
        .put("TDS_GN0", "Global Notes Off")
        .put("TDS_GN1", "Global Notes On")
        // Hardware Checks
        .put("TDS_HWPlayback", "Check Playback Capabilities")
        // Highlight
        .put("TDS_Highlight0", "False")
        .put("TDS_Highlight1", "True")
        // Item Font Size
        .put("TDS_IF_S14", "14pt Items")
        // Item Tools Menu
        .put("TDS_ITM1", "On")
        .put("TDS_ITM0", "Off")
        // Language
        .put("ENU", "English")
        .put("ENU-Braille", "Braille English")
        .put("ESN", "Spanish")
        // Mark for Review
        .put("TDS_MfR0", "Mark for Review feature is disabled")
        .put("TDS_MfR1", "Mark for Review  feature is enabled")
        // Masking
        .put("TDS_Masking0", "Masking Not Available")
        .put("TDS_Masking1", "Masking Available")
        // Mute System Volume
        .put("TDS_Mute0", "Not Muted")
        .put("TDS_Mute1", "Muted")
        // Non-Embedded Accommodations (NEA)
        .put("NEA_Abacus", "Abacus")
        .put("NEA_AR", "Alternate Response Options")
        .put("NEA_Calc", "Calculator")
        .put("NEA_MT", "Multiplication Table")
        .put("NEA_NoiseBuf", "Noise Buffers")
        .put("NEA_RA_Stimuli", "Read Aloud Stimuli")
        .put("NEA_SC_WritItems", "Scribe Items (Writing)")
        .put("NEA_STT", "Speech-to-Text")
        .put("NEA0", "None")
        // Non-Embedded Designated Supports
        .put("4.02", "Color Contrast (printed) - nonembedded")
        .put("NEDS_BD", "Bilingual Dictionary")
        .put("NEDS_CC", "Color Contrast")
        .put("NEDS_CO", "Color Overlay")
        .put("NEDS_Mag", "Magnification")
        .put("NEDS_RA_Items", "Read Aloud Items")
        .put("NEDS_RA_Stimuli", "Read Aloud Stimuli")
        .put("NEDS_SC_Items", "Scribe Items (Non-Writing)")
        .put("NEDS_SS", "Separate Settiing")
        .put("NEDS_TArabic", "Glossary - Arabic")
        .put("NEDS_TCantonese", "Glossary - Cantonese")
        .put("NEDS_TFilipino", "Glossary - Filipino")
        .put("NEDS_TKorean", "Glossary - Korean")
        .put("NEDS_TMandarin", "Glossary - Mandarin")
        .put("NEDS_TPunjabi", "Glossary - Punjabi")
        .put("NEDS_TransDirs", "Translated Test Directions")
        .put("NEDS_TRussian", "Glossary - Russian")
        .put("NEDS_TSpanish", "Glossary - Spanish")
        .put("NEDS_TUkrainian", "Glossary - Ukrainian")
        .put("NEDS_TVietnamese", "Glossary - Vietnamese")
        .put("NEDS0", "None")
        // Passage Font Size
        .put("TDS_F_S14", "14pt")
        // Permissive Mode
        .put("TDS_PM0", "Permissive Mode Disabled")
        .put("TDS_PM1", "Permissive Mode Enabled")
        // Print on Request
        .put("TDS_PoD0", "None")
        .put("TDS_PoD_Item", "Items")
        .put("TDS_PoD_Stim", "Stimuli")
        .put("TDS_PoD_Stim&TDS_PoD_Item", "Stimuli&Items")
        // Print Size/Zoom
        .put("TDS_PS_L0", "No default zoom applied")
        .put("TDS_PS_L1", "Level 1")
        .put("TDS_PS_L2", "Level 2")
        .put("TDS_PS_L3", "Level 3")
        .put("TDS_PS_L4", "Level 4")
        // Review Screen Layout
        .put("TDS_RSL_ListView", "List")
        // Streamlined Mode
        .put("TDS_SLM0", "Off")
        .put("TDS_SLM1", "On")
        // Strikethrough
        .put("TDS_ST0", "Off")
        .put("TDS_ST1", "On")
        // Student Comments
        .put("TDS_SC0", "Off")
        .put("TDS_SCNotepad", "On")
        // System Volume Control
        .put("TDS_SVC1", "Show widget")
        // Test Progress Indicator
        .put("TDS_TPI_ResponsesFix", "Show indicator as a fraction and adjust to test length")
        // Test Shell
        .put("TDS_TS_Modern", "Modern skin")
        .put("TDS_TS_Universal", "Universal Shell")
        // Thesaurus
        .put("TDS_TH0", "Off")
        .put("TDS_TH_TA", "MerriamWebsters Collegiate Thesaurus")
        // Text-to-Speech (TTS)
        .put("TDS_TTS0", "None")
        .put("TDS_TTS_Item", "Items")
        .put("TDS_TTS_Stim", "Stimuli")
        .put("TDS_TTS_Stim&TDS_TTS_Item", "Stimuli&Items")
        // TTS Audio Adjustments
        .put("TDS_TTSAA0", "TTS Audio Adjustments Off")
        .put("TDS_TTSAA_Volume&TDS_TTSAA_Pitch&TDS_TTSAA_Rate&TDS_TTSAA_SelectVP", "All Options")
        // TTS Pausing
        .put("TDS_TTSPause0", "TTS Pausing Off")
        .put("TDS_TTSPause1", "TTS Pausing OffTTS Pausing On")
        // TTX Rules
        .put("TDS_TTX_A203", "A203")
        .put("TDS_TTX_A206", "A206")
        // Tutorial
        .put("TDS_T0", "False")
        .put("TDS_T1", "True")
        // Word List
        .put("TDS_WL_ArabicGloss", "Arabic Glossary")
        .put("TDS_WL_ArabicGloss&TDS_WL_Glossary", "Arabic & English Glossary")
        .put("TDS_WL_CantoneseGloss", "Cantonese Glossary")
        .put("TDS_WL_CantoneseGloss&TDS_WL_Glossary", "Cantonese & English Glossary")
        .put("TDS_WL_ESNGlossary", "Spanish Glossary")
        .put("TDS_WL_ESNGlossary&TDS_WL_Glossary", "Spanish & English Glossary")
        .put("TDS_WL_Glossary", "English Glossary")
        .put("TDS_WL_Glossary&TDS_WL_ArabicGloss", "English & Arabic Glossary")
        .put("TDS_WL_Glossary&TDS_WL_CantoneseGloss", "English & Cantonese Glossary")
        .put("TDS_WL_Glossary&TDS_WL_ESNGlossary", "English & Spanish Glossary")
        .put("TDS_WL_Glossary&TDS_WL_KoreanGloss", "English & Korean Glossary")
        .put("TDS_WL_Glossary&TDS_WL_MandarinGloss", "English & Mandarin Glossary")
        .put("TDS_WL_Glossary&TDS_WL_PunjabiGloss", "English & Punjabi Glossary")
        .put("TDS_WL_Glossary&TDS_WL_RussianGloss", "English & Russian Glossary")
        .put("TDS_WL_Glossary&TDS_WL_TagalGloss", "English & Filipino Glossary")
        .put("TDS_WL_Glossary&TDS_WL_UkrainianGloss", "English & Ukrainian Glossary")
        .put("TDS_WL_Glossary&TDS_WL_VietnameseGloss", "English & Vietnamese Glossary")
        .put("TDS_WL_KoreanGloss", "TDS_WL_KoreanGloss&TDS_WL_Glossary")
        .put("TDS_WL_KoreanGloss&TDS_WL_Glossary", "Korean & English Glossary")
        .put("TDS_WL_MandarinGloss", "Mandarin Glossary")
        .put("TDS_WL_MandarinGloss&TDS_WL_Glossary", "Mandarin & English Glossary")
        .put("TDS_WL_PunjabiGloss", "Punjabi Glossary")
        .put("TDS_WL_PunjabiGloss&TDS_WL_Glossary", "Punjabi & English Glossary")
        .put("TDS_WL_RussianGloss", "Russian Glossary")
        .put("TDS_WL_RussianGloss&TDS_WL_Glossary", "Russian & English Glossary")
        .put("TDS_WL_TagalGloss", "Filipino Glossary")
        .put("TDS_WL_TagalGloss&TDS_WL_Glossary", "Filipino & English Glossary")
        .put("TDS_WL_UkrainianGloss", "Ukrainian Glossary")
        .put("TDS_WL_UkrainianGloss&TDS_WL_Glossary", "Ukrainian & English Glossary")
        .put("TDS_WL_VietnameseGloss", "Vietnamese Glossary")
        .put("TDS_WL_VietnameseGloss&TDS_WL_Glossary", "Vietnamese & English Glossary")
        .put("TDS_WL0", "No Glossary")
        .build();
}
