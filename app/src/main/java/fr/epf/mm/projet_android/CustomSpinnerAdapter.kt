package fr.epf.mm.projet_android

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(context: Context, items: List<String>, private val textColor: Int) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {
    private var isDialogOpen = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.setTextColor(textColor)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        if (isDialogOpen) {
            textView.setTextColor(Color.BLACK)
            textView.gravity = Gravity.CENTER
            view.setPadding(16, 32, 16, 32)
        } else {
            textView.setTextColor(textColor)
            textView.gravity = Gravity.CENTER
            view.setPadding(16, 16, 16, 16)
        }
        return view
    }

    fun setDialogOpen(open: Boolean) {
        isDialogOpen = open
        notifyDataSetChanged()
    }
}
