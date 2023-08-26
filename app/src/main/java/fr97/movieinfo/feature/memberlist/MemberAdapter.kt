package fr97.movieinfo.feature.memberlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr97.movieinfo.R
import kotlinx.android.synthetic.main.list_item_member.view.*

class MemberAdapter() : RecyclerView.Adapter<MemberViewHolder>() {

    private val members: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.list_item_member, parent, false)
        return MemberViewHolder(root)
    }

    override fun getItemCount(): Int = members.size

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.textView.text = members[position]
    }

    fun addNewMembers(newMembers: List<String>) {
        members.clear()
        members.addAll(newMembers)
        notifyDataSetChanged()
    }

}

class MemberViewHolder(val root: View) : RecyclerView.ViewHolder(root) {

    val textView : TextView = root.findViewById(R.id.textMemberName)

}