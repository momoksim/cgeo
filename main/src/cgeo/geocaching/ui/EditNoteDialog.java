package cgeo.geocaching.ui;

import butterknife.ButterKnife;

import cgeo.geocaching.R;
import cgeo.geocaching.activity.Keyboard;
import cgeo.geocaching.ui.dialog.Dialogs;

import org.eclipse.jdt.annotation.NonNull;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;

public class EditNoteDialog extends DialogFragment {

    public interface EditNoteDialogListener {
        void onFinishEditNoteDialog(final String inputText);
    }

    public static final String ARGUMENT_INITIAL_NOTE = "initialNote";

    private EditText mEditText;
    private EditNoteDialogListener listener;

    public static EditNoteDialog newInstance(final String initialNote, final EditNoteDialogListener listener) {
        final EditNoteDialog dialog = new EditNoteDialog();

        final Bundle arguments = new Bundle();
        arguments.putString(EditNoteDialog.ARGUMENT_INITIAL_NOTE, initialNote);
        dialog.setArguments(arguments);
        dialog.listener = listener;

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final @NonNull FragmentActivity activity = getActivity();
        final View view = View.inflate(new ContextThemeWrapper(activity, R.style.dark), R.layout.fragment_edit_note, null);
        mEditText = ButterKnife.findById(view, R.id.note);
        final String initialNote = getArguments().getString(ARGUMENT_INITIAL_NOTE);
        if (initialNote != null) {
            mEditText.setText(initialNote);
            Dialogs.moveCursorToEnd(mEditText);
            getArguments().remove(ARGUMENT_INITIAL_NOTE);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.cache_personal_note);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        listener.onFinishEditNoteDialog(mEditText.getText().toString());
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int whichButton) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        new Keyboard(activity).showDelayed(mEditText);
        return dialog;
    }
}
