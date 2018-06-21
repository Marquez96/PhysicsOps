package marquez.physicsops.Classes;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by Carlos-Torre on 12/05/2018.
 */

public class AxisValuesFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;
    private String letter;

    public AxisValuesFormatter(String letter) {
        this.letter = letter;
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(letter.equals("")){
            return "";
        }
        return mFormat.format(value) + " " + letter;
    }
}
