
---
<div align="center">
	<img src="https://mod-assets.upcraft.dev/promo/general/multiloader_banner.png" width="480" alt="">
</div>

---

Velvet is a multiloader library for making and using your own OpenGL shaders based off of Satin. I highly suggest supporting the Satin developers, but don't pester them with bug reports if you're using Velvet.

---

## Adding Velvet to your project

If your project is Fabric only, please use [Satin](https://github.com/Ladysnake/Satin) instead.

You can add Velvet by putting the following in your `build.gradle`:
```gradle
repositories {
	maven {
		name = "Up-Mods & Cammie's Corner"
		url = "https://maven.uuid.gg/releases"
	}
}
```
For Fabric (Ignore this if you're only using NeoForge):
```gradle
dependencies {
	modImplementation "dev.cammiescorner.velvet:Velvet-Common:${velvet_version}"
	modImplementation "dev.cammiescorner.velvet:Velvet-Fabric:${velvet_version}"
	
	// If you want to jar-in-jar Velvet
	include "dev.cammiescorner.velvet:Velvet-Common:${velvet_version}"
	include "dev.cammiescorner.velvet:Velvet-Fabric:${velvet_version}"
}
```
For NeoForge:
```gradle
dependencies {
	implementation "dev.cammiescorner.velvet:Velvet-Common:${velvet_version}"
	implementation "dev.cammiescorner.velvet:Velvet-NeoForge:${velvet_version}"
	
	// If you want to jar-in-jar Velvet
	jarJar(dev.cammiescorner.velvet:Velvet-Common:${velvet_version}) {
		transitive = false
		version {
			strictly "[${velvet_version},)"
			prefer ${velvet_version}
		}
	}
	jarJar(dev.cammiescorner.velvet:Velvet-NeoForge:${velvet_version}) {
		transitive = false
		version {
			strictly "[${velvet_version},)"
			prefer ${velvet_version}
		}
	}
}
```

Then add the library version to your `gradle.properties` file:
```properties
# Velvet version
velvet_version=0.x.x
```
Or your `libs.versions.toml` file if you're using one of those instead:
```toml
# Velvet version
velvet_version = "0.x.x"
```

For the latest version of Velvet, please check the [GitHub Releases](https://github.com/CammiePone/Velvet/releases).

---

## Documentation

[Satin's wiki](https://github.com/Ladysnake/Satin/wiki) provides documentation that should function for Velvet. Please note that some class and method names were changed to match with Mojmap names, so do keep that in mind when working with Velvet.

---

## Pull Requests

Unless it's to fix a bug exclusive to Velvet, you will need to make a PR with the same changes to Satin, and have it be accepted in order for it to be merged into Velvet. I want to keep parity with Satin, as it simplifies a lot of things between the two mods.

---

<p align="center">
	<a href="https://cammiescorner.dev/discord"><img src="https://cammiescorner.dev/images/extras/discord.png" width="150" height="150" title="Join my Discord" alt="The Discord icon, which is a blue circle with half in shadow. In the middle is a weird little robot-helmet-looking-thingy."></a>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<a href="https://www.ko-fi.com/camellias"><img src="https://cammiescorner.dev/images/extras/kofi.png" width="150" height="150" title="Support me on Ko-Fi" alt="The Ko-Fi logo, a sky-blue circle with a white coffee mug with a red heart on it in the middle."></a>
</p>
