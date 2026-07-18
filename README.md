# Immersive Lanterns — Minecraft 26.1.2 Port

> [!IMPORTANT]
> This repository is a community fork of
> [Toni's original Immersive Lanterns project](https://github.com/txnimc/ImmersiveLanterns).
> The original mod and concept were created by Toni. This fork is maintained by
> Naho and ports the mod to Minecraft 26.1.2.

Immersive Lanterns lets players equip lanterns at their waist. Equipped
lanterns move with the player and emit dynamic light, keeping both hands free
while exploring.

![Immersive Lanterns banner](banner.png)

## Minecraft 26.1.2 port

The current release target is **Minecraft 26.1.2 with Fabric**. This port:

- uses Trinkets Updated for the dedicated waist lantern slot;
- supports vanilla lanterns and compatible modded `LanternBlock` items;
- restores dynamic lighting through LambDynamicLights;
- adds configurable lantern side, scale, physics strength, and damping;
- includes an in-game configuration screen through Mod Menu;
- provides English and Brazilian Portuguese translations;
- improves lantern movement while walking, sprinting, crouching, falling,
  swimming, fall-flying, and riding.

Older mapped Minecraft targets remain in the source tree, but the actively
maintained port in this fork is `26.1.2-fabric`.

## Requirements

- Minecraft 26.1.2
- Fabric Loader 0.19.3 or newer
- Fabric API
- Trinkets Updated 4.0.0-beta.3+26.1 or newer
- LambDynamicLights 4.10.2 or newer
- Mod Menu 18.0.0 or newer (optional, for the configuration screen)

## Installation

1. Install Fabric Loader and Fabric API for Minecraft 26.1.2.
2. Install Trinkets Updated and LambDynamicLights.
3. Download the latest `.jar` from this fork's
   [Releases page](https://github.com/eoNaho/ImmersiveLanterns-Port/releases).
4. Place the downloaded files in your Minecraft `mods` directory.

Please report problems with the 26.1.2 port in this fork's
[issue tracker](https://github.com/eoNaho/ImmersiveLanterns-Port/issues), not in
the original project's tracker.

## Building from source

The 26.1.2 Fabric artifact can be built with:

```bash
./gradlew :26.1.2-fabric:build
```

The generated mod is written to:

```text
versions/26.1.2-fabric/build/libs/immersivelanterns-fabric-1.0.5-26.1.2.jar
```

## Credits and license

- **Toni** — creator of the original Immersive Lanterns mod.
- **Naho** — maintainer of the Minecraft 26.1.2 port.
- **Fyoncle / Useful Lanterns** — source of adapted positioning and orientation
  constants; see [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

This fork remains subject to the license in [LICENSE.md](LICENSE.md). The
original project is available at
[txnimc/ImmersiveLanterns](https://github.com/txnimc/ImmersiveLanterns), and the
source for this port is maintained at
[eoNaho/ImmersiveLanterns-Port](https://github.com/eoNaho/ImmersiveLanterns-Port).
