package marquez.physicsops.Activities;

/**
 * Created by Carlos-Torre on 18/02/2018.
 */
import android.view.View.OnFocusChangeListener;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.EditText;
import java.util.Calendar;
import android.content.Context;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import android.view.View;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.widget.TextView;

class SetDate implements OnFocusChangeListener, OnDateSetListener {

    private TextView editText;
    private Calendar myCalendar;
    private Context ctx;

    public SetDate(TextView editText, Context ctx, Calendar cal){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        myCalendar = cal;
        this.ctx = ctx;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        // this.editText.setText();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText(sdformat.format(myCalendar.getTime()));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(hasFocus){
            DatePickerDialog picker = new DatePickerDialog(ctx, this, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        }
    }

}