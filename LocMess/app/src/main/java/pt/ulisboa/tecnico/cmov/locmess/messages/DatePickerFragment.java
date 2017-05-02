package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import pt.ulisboa.tecnico.cmov.locmess.R;

/**
 * Created by Valentyn on 01-05-2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private int min;
    private int hours;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        CheckBox cks = (CheckBox) this.getActivity().findViewById(R.id.checkboxUseStartTimeLimit);
        CheckBox cke = (CheckBox) this.getActivity().findViewById(R.id.checkboxUseEndTimeLimit);
        TextView tvs = (TextView) this.getActivity().findViewById(R.id.textViewSDate);

        if(cks.isChecked() && !tvs.isEnabled())
            cks.setChecked(false);
        else
            cke.setChecked(false);
    }

    public void populateTime(int m, int h){
        min=m;
        hours=h;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        CheckBox cks = (CheckBox) this.getActivity().findViewById(R.id.checkboxUseStartTimeLimit);
        TextView tvs = (TextView) this.getActivity().findViewById(R.id.textViewSDate);
        TextView tve = (TextView) this.getActivity().findViewById(R.id.textViewEDate);

        String m=""+min;
        String h=""+hours;
        String d=""+day;
        String mo=""+(month+1);

        if(min<10)
            m="0"+min;
        if(hours<10)
            h="0"+hours;
        if(day<10)
            d="0"+day;
        if(month<10)
            mo="0"+(month+1);

        if (cks.isChecked() && !tvs.isEnabled()) {
            tvs.setText(year+"-"+mo+"-"+d+"T"+h+":"+m+":00");
            tvs.setEnabled(true);
        }
        else {
            tve.setText(year+"-"+mo+"-"+d+"T"+h+":"+m+":00");
            tve.setEnabled(true);
        }
    }
}
