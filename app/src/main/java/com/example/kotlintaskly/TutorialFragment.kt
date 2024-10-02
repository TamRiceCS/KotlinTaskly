package com.example.kotlintaskly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class TutorialFragment : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val texts = ArrayList<String>()
    val imgs = ArrayList<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_tutorial, container, false)
        val skipButton: Button = view.findViewById(R.id.skipButton)
        skipButton.setOnClickListener(this);

        val vp: ViewPager2 = view.findViewById(R.id.window)
        vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        imgs.add(R.drawable.task_capture)
        imgs.add(R.drawable.diary_capture)
        imgs.add(R.drawable.draft_capture)
        imgs.add(R.drawable.settings_capture)

        texts.add("Tasks are divided into 3 sections. They can be drag and dropped between sections and swiped to be deleted")
        texts.add("Diary entries are divided into drafts and entries. They can be swiped to be deleted")
        texts.add("Drafts are created by discarding an entry")
        texts.add("Don't want to set a passcode now? No worries. You can always set one later.")

        vp.adapter = TutorialAdapter(texts, imgs)

        val indicator: CircleIndicator3 = view.findViewById(R.id.progress)
        indicator.setViewPager(vp)



        return view
    }

    private fun replaceFragment(fragment : Fragment, identity: String) {
        val bundle = Bundle()
        bundle.putString("case", identity)
        fragment.arguments = bundle
        val fragManager = parentFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragmentContainerView, fragment)
        fragTransaction.commit()
    }

    override fun onClick(bttn: View) {
        if(bttn.id == R.id.skipButton) {
            replaceFragment(PasscodeFragment(), "new")
        }
    }
}