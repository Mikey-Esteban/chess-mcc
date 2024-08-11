public class Player {
  private String color;
  private boolean isChecked;

  public Player(String color) {
    this.color = color;
    this.isChecked = false;
  }

  public String getColor() {
    return this.color;
  }

  public boolean isChecked() {
    return this.isChecked;
  }

  public void setChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }
  
}
