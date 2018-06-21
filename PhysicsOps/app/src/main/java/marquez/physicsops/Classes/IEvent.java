package marquez.physicsops.Classes;

import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Carlos-Torre on 27/05/2018.
 */

public interface IEvent {

    public Calendar getCalendar();

    public Integer getDay();

    public Integer getMonth();

    public TextView getTextViewEvent(Context context);

    public String getTitle();

    public String getType();

    public Integer getYear();

    public Calendar getCal();

    public int getId();

    public void setTitle(String title);

    public void setType(String type);

    public void setCal(String dateTxt);

    public Integer getImportance();

    public void setImportance(Integer importance);

    public float getDuration();

    public void setDuration(float duration);


    public String getShortTitle();
}
