import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintaskly.R
import com.example.kotlintaskly.TaskData

class TaskAdapter(private val mList: ArrayList<TaskData>, private val orgin: String) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates each individual task card
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_task, parent, false)
        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // used to bind info to each task
        var taskDescr = mList.get(position).task
        holder.taskText.text = taskDescr
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // add items to list
    fun addAndInform(item : TaskData, position: Int) {
        mList.add(position, item)
        notifyDataSetChanged()
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val notifBell: ImageButton = itemView.findViewById(R.id.setReminder)
        val taskText: TextView = itemView.findViewById(R.id.taskText)
    }
}
