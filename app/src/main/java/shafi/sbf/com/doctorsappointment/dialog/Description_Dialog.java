package shafi.sbf.com.doctorsappointment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import shafi.sbf.com.doctorsappointment.R;

public class Description_Dialog extends AppCompatDialogFragment {

    private EditText descriptionEdt;
    private DescriptionDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.description_dialog_layout, null);

        builder.setView(view)
                .setTitle("Description")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String details = descriptionEdt.getText().toString().trim();
                        if (details.isEmpty()){
                            descriptionEdt.setError(getString(R.string.required_field));
                            Toast.makeText(getContext(), "Failed. Enter your problems first...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listener.onSubmit(details);
                    }
                });

        descriptionEdt = view.findViewById(R.id.descriptionEdt);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (DescriptionDialogListener) context;
    }

    public interface DescriptionDialogListener{
         void onSubmit(String description);
    }
}
