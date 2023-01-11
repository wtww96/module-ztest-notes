/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dp.notes.ui.keyboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dp.notes.R

/**
 * author Dq
 * date on 2022/11/25
 * description
 */
internal class ConversationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            ITEM_TYPE_MESSAGE_SELF -> {
                inflater.inflate(R.layout.notes_item_message_bubble_self, parent, false)
            }
            else -> {
                inflater.inflate(R.layout.notes_item_message_bubble_other, parent, false)
            }
        }
        return MessageHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.bubble_message).apply {
            text = "粉丝等级开发加快速度".plus(position.toString())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) ITEM_TYPE_MESSAGE_OTHER else ITEM_TYPE_MESSAGE_SELF
    }

    override fun getItemCount(): Int = 50

    companion object {
        const val ITEM_TYPE_MESSAGE_SELF = 0
        const val ITEM_TYPE_MESSAGE_OTHER = 1
    }
}

private class MessageHolder(view: View) : RecyclerView.ViewHolder(view)
