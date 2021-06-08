package com.cefer.mviarchitecture.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cefer.mviarchitecture.databinding.LayoutBlogListItemBinding
import com.cefer.mviarchitecture.model.BlogPost

class BlogListAdapter(private val interaction : Interaction? = null) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {
        
        override fun areItemsTheSame(oldItem : BlogPost , newItem : BlogPost) : Boolean {
            return oldItem.pk == newItem.pk
        }
        
        override fun areContentsTheSame(oldItem : BlogPost , newItem : BlogPost) : Boolean {
            return oldItem == newItem
        }
        
    }
    private val differ = AsyncListDiffer(this , DIFF_CALLBACK)
    
    
    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : RecyclerView.ViewHolder {
        
        return BlogPostViewHolder(
                LayoutBlogListItemBinding.inflate(
                        LayoutInflater.from(parent.context) ,
                        parent ,
                        false
                ) ,
                interaction
        )
    }
    
    override fun onBindViewHolder(holder : RecyclerView.ViewHolder , position : Int) {
        when (holder) {
            is BlogPostViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }
    
    override fun getItemCount() : Int {
        return differ.currentList.size
    }
    
    fun submitList(list : List<BlogPost>) {
        differ.submitList(list)
    }
    
    class BlogPostViewHolder(
            private val layoutBlogListItemBinding : LayoutBlogListItemBinding ,
            private val interaction : Interaction?
    ) : RecyclerView.ViewHolder(layoutBlogListItemBinding.root) {
        
        fun bind(item : BlogPost) {
            layoutBlogListItemBinding.root.setOnClickListener {
                interaction?.onItemSelected(adapterPosition , item)
            }
            layoutBlogListItemBinding.blogTitle.text = item.title
            
            Glide.with(layoutBlogListItemBinding.root.context)
                    .load(item.image)
                    .into(layoutBlogListItemBinding.blogImage)
            
        }
    }
    
    interface Interaction {
        
        fun onItemSelected(position : Int , item : BlogPost)
    }
}