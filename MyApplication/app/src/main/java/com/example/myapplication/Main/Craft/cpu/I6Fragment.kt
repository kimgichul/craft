import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class I6Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_i6, container, false)

        val i6Block1 = view.findViewById<View>(R.id.i6_block1)
        val i6Block2 = view.findViewById<View>(R.id.i6_block2)
        val i6Block3 = view.findViewById<View>(R.id.i6_block3)
        i6Block1.setOnClickListener {
            activity?.finish()
        }
        i6Block2.setOnClickListener {
            activity?.finish()
        }
        i6Block3.setOnClickListener {
            activity?.finish()
        }

        return view
    }
}
