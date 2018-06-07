package domain;

import java.util.Objects;

/**
 * Child class of Question, used to implement time-limits
 */
public class TimeQuestion extends Question{
    private int time = 0 ;

    public TimeQuestion(){

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeQuestion that = (TimeQuestion) o;
        return time == that.time;
    }

    @Override
    public int hashCode() {

        return Objects.hash(time);
    }
}
