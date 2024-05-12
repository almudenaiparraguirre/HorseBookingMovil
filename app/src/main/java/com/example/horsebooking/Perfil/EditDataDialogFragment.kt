import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.example.horsebooking.R


class EditDataDialogFragment : DialogFragment() {
    var listener: EditDataListener? = null

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val inflater: LayoutInflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_edit_data, null)

        val editTextUsername: EditText = view.findViewById(R.id.editTextUsername)
        val editTextEmail: EditText = view.findViewById(R.id.editTextEmail)

        builder.setView(view)
            .setPositiveButton("Guardar") { dialog, id ->
                val nombre = editTextUsername.text.toString()
                val email = editTextEmail.text.toString()
                listener?.onUpdateData(nombre, email)
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                dialog?.cancel()
            }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EditDataListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement EditDataListener")
        }
    }
}

interface EditDataListener {
    fun onUpdateData(username: String, email: String)
}