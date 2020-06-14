package ca.judacribz.zooatlanta.animals

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import ca.judacribz.zooatlanta.R
import ca.judacribz.zooatlanta.animal_details.AnimalDetailsActivity
import ca.judacribz.zooatlanta.models.Animal
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_animal.view.ivHomepageAnimalImage
import kotlinx.android.synthetic.main.item_animal.view.tvAnimalName
import kotlinx.android.synthetic.main.item_animal.view.tvDiet
import kotlinx.android.synthetic.main.item_animal.view.tvRange
import kotlinx.android.synthetic.main.item_animal.view.tvScientificName
import kotlinx.android.synthetic.main.item_animal.view.tvStatus

class AnimalAdapter(val animals: ArrayList<Animal> = ArrayList()) :
    RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    companion object {
        const val EXTRA_ANIMAL = "ca.judacribz.week2weekend.animals.EXTRA_ANIMAL"
    }

    private val animalBmps: MutableMap<String?, Bitmap>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_animal,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(animals[position])
    }

    override fun getItemCount(): Int {
        return animals.size
    }

    fun addAnimal(animal: Animal) {
        animals.add(animal)
        notifyItemInserted(itemCount)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                Intent(it.context, AnimalDetailsActivity::class.java).apply {
                    putExtras(bundleOf(EXTRA_ANIMAL to animals[adapterPosition]))
                    it.context.startActivity(this)
                }
            }
        }

        fun bind(animal: Animal) {
            itemView.tvAnimalName.text = animal.name
            itemView.tvScientificName.text = animal.scientificName
            itemView.tvDiet.text = animal.diet
            itemView.tvStatus.text = animal.status
            itemView.tvRange.text = animal.range
            Glide
                .with(itemView.context)
                .load(animal.imgUrl)
                .into(itemView.ivHomepageAnimalImage)
        }
    }

    init {
        animalBmps = HashMap()
    }
}