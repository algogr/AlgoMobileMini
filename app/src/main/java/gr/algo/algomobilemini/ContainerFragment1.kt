package gr.algo.algomobilemini


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



/**
 * A simple [Fragment] subclass.
 * Use the [ContainerFragment1.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ContainerFragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
        fragmentTransaction.add(R.id.containerFrame1,Tab1Fragment())

        fragmentTransaction.commit()
        return inflater.inflate(R.layout.fragment_container_fragment1, container, false)
    }



}
