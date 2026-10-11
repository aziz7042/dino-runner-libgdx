# Dino Runner · libGDX

A Java/libGDX endless runner created as a university team project. The game extends the familiar runner formula with ducking, flying obstacles, custom polygon hitboxes and audio feedback.

## Features

- Walking, ducking and parabolic jump movement
- Randomized ground and flying obstacles
- Arrow and fireball obstacle variants
- Custom polygon hitboxes loaded from assets
- Convex-polygon collision detection
- Animation and sound integration

## Technology

`Java` · `libGDX` · `Gradle` · `Object-oriented design` · `2D collision detection`

## Run

```bash
./gradlew lwjgl3:run
```

On Windows:

```powershell
.\gradlew.bat lwjgl3:run
```

The final source compiles successfully with the included Gradle wrapper. The project currently contains no automated tests.

## Provenance

Created by **Group 7** for *Software Engineering & IT Project Management* at Otto von Guericke University Magdeburg, winter semester 2024. This repository preserves the available group attribution and does not present the complete implementation as individual work.

### Aziz's documented contribution

The team's contribution record credits Aziz with the collision-detection work, the jump handler and parabolic jump function, the restart function, and the restart button/UI flow. The collision and jump implementations are present in this curated final source snapshot; the statement does not present the remaining team implementation as individual work.

## Asset provenance

The game-specific images, sprite sheets, hitbox outlines and `drop.mp3` were created by the university project team for Dino Runner. The libGDX logo and default UI skin files are framework/template assets and are not claimed as team-created. Details are recorded in [`ASSET_CREDITS.md`](ASSET_CREDITS.md).
