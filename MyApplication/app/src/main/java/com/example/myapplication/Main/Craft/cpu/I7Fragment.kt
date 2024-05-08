import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class I7Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_i7, container, false)

        val i7Block1 = view.findViewById<View>(R.id.i7_block1)
        val i7Block2 = view.findViewById<View>(R.id.i7_block2)
        val i7Block3 = view.findViewById<View>(R.id.i7_block3)
        i7Block1.setOnClickListener {
            activity?.finish()
        }
        i7Block2.setOnClickListener {
            activity?.finish()
        }
        i7Block3.setOnClickListener {
            activity?.finish()
        }

        return view
    }
}
