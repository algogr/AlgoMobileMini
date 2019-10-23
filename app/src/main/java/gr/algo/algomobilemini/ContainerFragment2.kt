package gr.algo.algomobilemini

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ContenairFragmen2.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ContenairFragmen2.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ContainerFragment2 : Fragment() {

    lateinit var basket:Tab2Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
        fragmentTransaction.add(R.id.containerFrame2,basket)

        fragmentTransaction.commit()
        return inflater.inflate(R.layout.fragment_container_fragment2, container, false)
    }









}
