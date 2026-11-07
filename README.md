# Tier Tagger by Ugluskettiy (Fabric, yalnizca istemci)

Oyuncu isimlerinin yanina renkli tier etiketleri ve mod ikonlari ekleyen, TAMAMEN KOZMETIK bir mod.
Etiketler sadece senin ekraninda cizilir (kafa ustu isim, F5'te kendi etiketin, TAB listesi).
Sunucuya paket gitmez, oyun mekanigine dokunmaz, ag erisimi yoktur.
Tek yazdigi dosya: `config/tiertagger.json`.

Gorunum (duzen ayardan secilir):
- `left`:  `[ikon] LT1 | Isim`
- `right`: `Isim | LT2 [ikon]`

## Ayar ekrani
ESC > Mods (Mod Menu) > Tier Tagger > ayar simgesi. Genel ayarlar (acik/kapali, duzen, varsayilan tier ve ikon)
ve "Oyuncular" sekmesi (her satir: `isim tier [mod]`, ornek `Steve HT1 sword`).
Mod Menu ve Cloth Config kurulu olmali.

Mod ikonlari: crystal, sword, axe, pickaxe, uhc, pot, netherite, mace (none = ikonsuz).

## Komutlar (sohbette)
- `/tiertagger set <oyuncu> <tier> [mod]`  ornek: `/tiertagger set Steve HT1 sword`
- `/tiertagger remove <oyuncu>`
- `/tiertagger default <tier|off>`
- `/tiertagger style <left|right>`
- `/tiertagger toggle`
- `/tiertagger list`

## Iki ayri proje = iki jar (neden?)
Minecraft 26.1'de oyun obfuscate edilmemis hale geldi ve Fabric, Yarn mappings'i birakip Mojang isimlerine gecti.

| Klasor    | Kapsadigi surumler   | Isimlendirme | JDK                         |
|-----------|----------------------|--------------|-----------------------------|
| `legacy/` | 1.20.1 - 1.21.11     | Yarn         | 17 (21 ile de calisir)      |
| `modern/` | 26.1 - 26.2          | Mojang       | `gradle.properties`'e bak   |

## Kurulum
`.minecraft/mods` icine at: Tier Tagger jar'i + Fabric API + **Mod Menu** + **Cloth Config** (surume uygun).

## Derleme
`legacy/`: JDK 17 ile `gradle wrapper` sonra `./gradlew build`.
`modern/`: once `gradle.properties` icindeki `DOLDUR` satirlarini doldur, sonra `./gradlew build`.

## Onemli notlar
- Hicbir kisim derlenip Minecraft'ta denenmedi; ilk derlemede kucuk duzeltmeler gerekebilir.
- Ikonlar mod icindeki font'a (`assets/minecraft/font/default.json`) eklenen ozel karakterlerdir;
  ikonlari degistirmek icin `assets/tiertagger/textures/font/icons.png` (8 ikon, 8x8) dosyasini duzenle.
- Tek oyunculu modda yerel sunucu da ayni JVM'de calistigi icin etiket oyun ici mesajlarda da
  gorunebilir; yine sadece sana.
