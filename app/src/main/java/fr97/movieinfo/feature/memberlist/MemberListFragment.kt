package fr97.movieinfo.feature.memberlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import fr97.movieinfo.R
import fr97.movieinfo.databinding.FragmentMemberListBinding
import kotlinx.android.synthetic.main.fragment_member_list.view.*

class MemberListFragment : Fragment() {

    private lateinit var binding: FragmentMemberListBinding

    private lateinit var memberAdapter: MemberAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member_list, container, false)
        val members = arguments?.getStringArrayList("members") ?: ArrayList()
        val type = arguments?.getString("type") ?: ""
        memberAdapter = MemberAdapter()
        binding.recyclerViewMember.adapter = memberAdapter
        binding.recyclerViewMember.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        if (members.isNotEmpty()) {
            onSuccess(members)
        } else {
            onError("No $type details.")
        }
        return binding.root
    }

    private fun onSuccess(members: List<String>) {
        binding.textOnError.visibility = View.GONE
        memberAdapter.addNewMembers(members)
    }

    private fun onError(message: String) {
        binding.textOnError.text = message
        binding.textOnError.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(members: ArrayList<String>, type: String): MemberListFragment {
            val args = Bundle()
            args.putStringArrayList("members", members)
            args.putString("type", type)
            val fragment = MemberListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
