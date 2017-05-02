package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import pt.ulisboa.tecnico.cmov.locmess.R;

/**
 * Created by Valentyn on 01-05-2017.
 */

public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
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

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.populateTime(minute,hourOfDay);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
