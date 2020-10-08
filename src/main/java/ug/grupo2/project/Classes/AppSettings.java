package ug.grupo2.project.Classes;

public final class AppSettings {
  boolean DarkMode;
  private Palette primaryPalette;
  private Palette secondaryPalette;

  public AppSettings(boolean DarkMode, Palette primaryPalette, Palette secondaryPalette) {
    this.DarkMode = DarkMode;
    this.primaryPalette = primaryPalette;
    this.secondaryPalette = secondaryPalette;
  }

  public boolean isDarkMode() {
    return this.DarkMode;
  }

  public boolean getDarkMode() {
    return this.DarkMode;
  }

  public void setDarkMode(boolean DarkMode) {
    this.DarkMode = DarkMode;
  }

  public Palette getPrimaryPalette() {
    return this.primaryPalette;
  }

  public void setPrimaryPalette(Palette primaryPalette) {
    this.primaryPalette = primaryPalette;
  }

  public Palette getSecondaryPalette() {
    return this.secondaryPalette;
  }

  public void setSecondaryPalette(Palette secondaryPalette) {
    this.secondaryPalette = secondaryPalette;
  }

  public Palette getRelativePallete() {
    return this.DarkMode ? this.primaryPalette : this.secondaryPalette;
  }

  public class ColorVariations {
    String main;
    String dark;
    String light;
    String contrastText;

    public ColorVariations(String main, String dark, String light, String contrastText) {
      this.main = main;
      this.dark = dark;
      this.light = light;
      this.contrastText = contrastText;
    }

    public String getMain() {
      return this.main;
    }

    public void setMain(String main) {
      this.main = main;
    }

    public String getDark() {
      return this.dark;
    }

    public void setDark(String dark) {
      this.dark = dark;
    }

    public String getLight() {
      return this.light;
    }

    public void setLight(String light) {
      this.light = light;
    }

    public String getContrastText() {
      return this.contrastText;
    }

    public void setContrastText(String contrastText) {
      this.contrastText = contrastText;
    }

  }

  public class Palette {
    ColorVariations primary;
    ColorVariations secondary;
    String background;

    public Palette(ColorVariations primary, ColorVariations secondary, String background) {
      this.primary = primary;
      this.secondary = secondary;
      this.background = background;
    }

    public ColorVariations getPrimary() {
      return this.primary;
    }

    public void setPrimary(ColorVariations primary) {
      this.primary = primary;
    }

    public ColorVariations getSecondary() {
      return this.secondary;
    }

    public void setSecondary(ColorVariations secondary) {
      this.secondary = secondary;
    }

    public String getBackground() {
      return this.background;
    }

    public void setBackground(String background) {
      this.background = background;
    }
  }
}
