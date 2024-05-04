package com.serdararici.travelguide.View

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.ai.client.generativeai.GenerativeModel
import com.serdararici.travelguide.Adapter.MessageAdapter
import com.serdararici.travelguide.BuildConfig
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ChatBotViewModel
import com.serdararici.travelguide.databinding.FragmentChatBotBinding

class ChatBotFragment : Fragment() {
    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelChatBot : ChatBotViewModel
    private var messageAdapter = MessageAdapter()
    private var messageList = mutableListOf<Pair<String, Int>>()
    private val apiKey = BuildConfig.API_KEY_GEMINI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBotBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val geminiAi = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )

        viewModelChatBot = ViewModelProvider(
            this,
            ChatBotViewModel.ChatViewModelFactory(geminiAi)
        ).get(ChatBotViewModel::class.java)

        setAdapter()
        sendMessage()
        observe()
    }

    private fun setAdapter(){
        val ll = LinearLayoutManager(requireContext())
        ll.stackFromEnd = true
        binding.rcChat.layoutManager = ll
        binding.rcChat.setHasFixedSize(true)
        binding.rcChat.adapter = messageAdapter
    }

    private fun sendMessage(){
        binding.ivSend.setOnClickListener {
            val userMessage = binding.evSend.text.toString().trim()
            viewModelChatBot.geminiChat(userMessage)
            messageList.add(Pair(userMessage,MessageAdapter.VIEW_TYPE_USER))
            messageAdapter.setMessages(messageList)
            scrollPosition()
            binding.evSend.setText("")
            binding.imagePromptProgress.visibility = View.VISIBLE
            val inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(it.windowToken,0)
        }
    }

    private fun observe(){
        viewModelChatBot.messageResponse.observe(viewLifecycleOwner, Observer { content->
            content.text?.let {
                messageList.add(Pair(it,MessageAdapter.VIEW_TYPE_GEMINI))
                messageAdapter.setMessages(messageList)
                binding.imagePromptProgress.visibility = View.GONE
                scrollPosition()
            }
        })
    }

    private fun scrollPosition(){
        binding.rcChat.smoothScrollToPosition(messageAdapter.itemCount - 1)
    }
}