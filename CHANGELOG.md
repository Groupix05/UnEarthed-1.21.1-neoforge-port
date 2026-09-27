# Changelog

## 3.2.0-1.21.1-neoforge — 2026-09-28

### Added
- Added Create zinc-ore variants for 12 surface and 11 deep Unearthed rocks, including Create-compatible drops, smelting, and crushing recipes.
- Added optional Create: The Factory Must Grow compatibility for lead, lithium, and nickel ores across 12 surface and 11 deep Unearthed rocks. The variants preserve TFMG generation, drops, tool requirements, and lithium crushing outputs.
- Added optional Create Propulsion: Simulated compatibility for platinum ore across 12 surface and 11 deep Unearthed rocks, preserving its world generation, iron-tool requirement, drops, smelting, blasting, and crushing outputs.

## 3.1.1-1.21.1-neoforge — 2026-09-19

### Added
- Added the `enable_create_andesite_reserve` common-config option, enabled by default. It can be disabled by pack authors.
- Added language files for German, Spanish (Spain and Mexico), Brazilian Portuguese, French, Polish, Japanese, Korean, and Turkish.
- Added localized resource keys for the common-config categories and options.
- Restored the Gradle wrapper files so the project can be built with `gradlew` / `gradlew.bat`.
- Updated the development environment to NeoForge `21.1.219`, the minimum required by Create `6.0.10`.

### Fixed
- Migrated all bundled crafting, smelting, blasting, and stonecutting recipes to the Minecraft 1.21.1 result format. The recipes now load at runtime instead of being discarded as malformed data.
- Made `enable_regolith_to_dirt_recipe` actually control the regolith-to-dirt recipe through a NeoForge recipe condition.
- Made `disable_all` stop the stone-replacer world-generation feature as intended.
- Removed references to non-existent `unearthed:iron_ore` and `unearthed:gold_ore` items from item tags.
- Restored the original Hyle base and deep-layer values for the default, limestone, sedimentary, and vanilla regions. In particular, grey limestone is no longer the base stone of every sedimentary region.
- Registered the deep-rock replacer in Overworld generation and constrained it to `Y=-1` and below. Deep rocks and their ore variants now generate without overwriting surface strata.

### Changed
- Completed the missing entries in British English, Ukrainian, and Simplified Chinese.
- Normalized all 14 language files to the same 535 keys and removed obsolete localization entries.
- Documented the Create and regolith configuration behavior in the README.
- Create andesite now generates after the stone replacer and can replace natural Unearthed rock types, keeping its gravel-equivalent frequency stable.

### Compatibility Notes
- This release does not add a required Create dependency. The andesite reserve activates only when Create is present.
- Existing terrain remains unchanged. Create andesite generation applies only to chunks generated after installing this version.
