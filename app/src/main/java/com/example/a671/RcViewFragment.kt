package com.example.a671

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.MediaStore
import android.system.SystemCleaner
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.a671.databinding.FragmentRcViewBinding
import kotlinx.coroutines.handleCoroutineException
import java.text.FieldPosition
import java.text.SimpleDateFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RcViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RcViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentRcViewBinding
    var mediaPlayer: MediaPlayer?=null
    lateinit var handler: Handler
    var position:Int = -1
    var serializable:Sound?=null
    var list:ArrayList<Sound>?=null
    var paus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRcViewBinding.inflate(inflater,container,false)
        handler = Handler(Looper.getMainLooper())

        updateEffect(20f,binding.imageBlue)
        updateEffect(1f,binding.layout1Blur)
        updateEffect(2f,binding.layout2Blur)



        serializable = arguments?.getSerializable("key") as Sound
        list = arguments?.getSerializable("key2") as ArrayList<Sound>

        if (serializable?.image==null){
            binding.image.setImageResource(R.drawable.bandixon)
            binding.imageBlue.setImageResource(R.drawable.bandixon)
        }else{
            binding.image.setImageURI(serializable!!.image)
            binding.imageBlue.setImageURI(serializable!!.image)
        }

        binding.name.text = serializable!!.artist
        binding.audioName.text = serializable!!.title


        if (mediaPlayer == null){
            mediastart(serializable?.id!!,list!!)

        }


        binding.contextMenu.setOnClickListener {
            findNavController().navigate(R.id.action_rcViewFragment_to_homFragment)
        }

        binding.playPauza.setOnClickListener {
            pauza()

        }

        binding.shep.setOnClickListener {

            keyingi(serializable!!,list!!)

        }

        binding.skip.setOnClickListener {
            oldingi(serializable!!,list!!)

        }


        binding.forward30.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition!!.plus(3000))
        }

        binding.reples30.setOnClickListener {
            mediaPlayer?.seekTo(mediaPlayer?.currentPosition!!.minus(3000))
        }



        binding.seekbar.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView")
        object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPlayer?.seekTo(progress)




                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer = null
    }

    private fun mediastart(position: Int,list:ArrayList<Sound>) {


        if (serializable?.image==null){
            binding.image.setImageResource(R.drawable.bandixon)
            binding.imageBlue.setImageResource(R.drawable.bandixon)
        }else{
            binding.image.setImageURI(serializable!!.image)
            binding.imageBlue.setImageURI(serializable!!.image)
        }
        binding.name.text = list[position!!].artist
        binding.audioName.text = list[position!!].title
        mediaPlayer = MediaPlayer.create(binding.root.context, list[position!!].path?.toUri())
        mediaPlayer?.start()

        var sek = mediaPlayer?.duration!!.div(1000).mod(100)
        var min = mediaPlayer?.duration!!.div(100000)
        binding.duration.text = "${min}:$sek"

        binding.seekbar.max = mediaPlayer?.duration!!
        binding.seekbar.setProgress(0)
        handler.postDelayed(runnable,10)
    }

    private fun pauza() {
        if (mediaPlayer?.isPlaying!!){
            binding.playPauza.setImageResource(R.drawable.outline_play_circle_outline_24)
            mediaPlayer?.pause()
            paus = true
        }else{
            binding.playPauza.setImageResource(R.drawable.outline_pause_circle_24)
            mediaPlayer?.start()
            paus = false
        }
    }

    private fun oldingi(serializable:Sound,list:ArrayList<Sound>) {
        mediaPlayer?.stop()
        if (position==-1){
            position = serializable.id!!.toInt()
        }

        if (0<position!!){
            position--

        }else{
            position = list.size-1
        }

        mediastart(position,list)
    }

    private fun keyingi(serializable:Sound,list:ArrayList<Sound>) {
        mediaPlayer?.stop()
        if (position==-1){
            position = serializable.id!!.toInt()
        }

        if (list.size-1>position!!){
            position++

        }else{
            position = 0
        }
        mediastart(position,list)
    }

    private var runnable = object:Runnable{
        override fun run() {


            binding.timePosition.text = "${binding.seekbar.progress.div(100000)}:${binding.seekbar.progress.div(1000).rem(100)}"

            if (position==-1){
                binding.listCount.text = "${serializable?.id!!+1}/${list?.size}"
            }else{
                binding.listCount.text = "${position+1}/${list?.size}"
            }


            if(!mediaPlayer?.isPlaying!!&& !paus){
                keyingi(serializable!!,list!!)
            }
            binding.seekbar.setProgress(mediaPlayer?.currentPosition!!)
            handler.postDelayed(this,10)

        }

    }

    fun updateEffect(progress: Float, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (progress > 0) {
                val blur = RenderEffect.createBlurEffect(
                    progress, progress, Shader.TileMode.CLAMP
                )
                view.setRenderEffect(blur)
            } else {
                view.setRenderEffect(null)
            }
        }

    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RcViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RcViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}