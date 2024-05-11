import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.example.horsebooking.R


class EditDataDialogFragment : DialogFragment() {
    @SuppressLint("MissingInflatedId")
    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
        val inflater: LayoutInflater = requireActivity().getLayoutInflater()
        val view: View = inflater.inflate(R.layout.dialog_edit_data, null)

        // Configura los campos de texto y botones aquí, por ejemplo:
        val editTextUsername: EditText = view.findViewById(R.id.editTextUsername)
        val editTextEmail: EditText = view.findViewById(R.id.editTextEmail)
        builder.setView(view)
            .setPositiveButton("Guardar", DialogInterface.OnClickListener { dialog, id ->
                // Aquí manejas la acción de guardar
                val username = editTextUsername.text.toString()
                val email = editTextEmail.text.toString()
                // Puedes pasar estos valores a la actividad a través de un método
            })
            .setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, id ->
                    this@EditDataDialogFragment.getDialog()?.cancel()
                })
        return builder.create()
    }
}