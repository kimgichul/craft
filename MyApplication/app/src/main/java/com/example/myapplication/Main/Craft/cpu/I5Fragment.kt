import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class I5Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_i5, container, false)

        val i5Block1 = view.findViewById<View>(R.id.i5_block1)
        val i5Block2 = view.findViewById<View>(R.id.i5_block2)
        val i5Block3 = view.findViewById<View>(R.id.i5_block3)
        i5Block1.setOnClickListener {
            activity?.finish()
        }
        i5Block2.setOnClickListener {
            activity?.finish()
        }
        i5Block3.setOnClickListener {
            activity?.finish()
        }

        return view
    }
}
