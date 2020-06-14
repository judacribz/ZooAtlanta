package ca.judacribz.zooatlanta.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.models.Category
import kotlinx.android.synthetic.main.item_category.view.tvCategoryName
import kotlinx.android.synthetic.main.item_category.view.tvDescription
import kotlinx.android.synthetic.main.item_category.view.tvNumSpecies

class CategoryAdapter(private val categories: ArrayList<Category> = ArrayList(),
                      val onCategoryClick: (category: Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_category,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size

    fun addCategory(category: Category) {
        categories.add(category)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category) = itemView.apply {
            setOnClickListener { onCategoryClick(category) }
            tvCategoryName.text = category.name
            tvNumSpecies.text = category.numSpecies.toString()
            tvDescription.text = category.description
        }
    }
}