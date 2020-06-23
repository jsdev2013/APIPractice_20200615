package com.jisu.apipractice_20200615.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.jisu.apipractice_20200615.R
import com.jisu.apipractice_20200615.datas.TopicReply
import com.jisu.apipractice_20200615.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class TopicReplyAdapter(
    val mContext:Context,
    val resId:Int,
    val mList:List<TopicReply>) : ArrayAdapter<TopicReply>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        tempRow?.let {

        }.let {
            tempRow = inf.inflate(R.layout.topic_reply_list_item, null)
        }

//        row가 절대 null 아님을 보장하면서 대입
        val row = tempRow!!

        val replyNickNameTxt = row.findViewById<TextView>(R.id.replyNickNameTxt)
        val replyContentTxt = row.findViewById<TextView>(R.id.replyContentTxt)
        val replyTimeTxt = row.findViewById<TextView>(R.id.replyTimeTxt)
        val replyBtn = row.findViewById<Button>(R.id.replyBtn)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val dislikeBtn = row.findViewById<Button>(R.id.dislikeBtn)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)

        val data = mList[position]

        replyNickNameTxt.text = data.writer.nickname
        replyContentTxt.text = data.content

        //  선택 진영 정보 반영
        selectedSideTitleTxt.text = "(${data.selectedSide.title})"

        val sdf = SimpleDateFormat("M월 d일 a h시 m분")

        replyTimeTxt.text = sdf.format(data.createdAt.time)

        // 좋아요 / 싫어요 / 답글 갯수 표시
        replyBtn.text = "답글 : ${data.replyCount.toString()}개"
        likeBtn.text = "좋아요 : ${data.likeCount.toString()}개"
        dislikeBtn.text = "싫어요 : ${data.dislikeCount.toString()}개"

        // 좋아요, 싫어요 클릭 이벤트
        val likeOrDislikeEvent = View.OnClickListener {
            // 좋아요를 누르면 is_like - true
            // 싫어요를 누르면 is_like - false
            val isLike = it.id == R.id.likeBtn

            ServerUtil.postRequestReplyLikeOrDislike(mContext,  data.id, isLike, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 목록을 뿌릴때 쓰는 data와 이름이 겹쳐서 변경 (dataObj) => data도 활용 예정
                    val dataObj = json.getJSONObject("data")
                    val reply = dataObj.getJSONObject("reply")

                    // data변수 내부 값중 좋아요 /싫어요 갯수 변경
                    data.likeCount = reply.getInt("like_count")
                    data.dislikeCount = reply.getInt("dislike_count")

                    // 리스트뷰에 뿌려지는 데이터에 내용 변경 => notifyDataSetChanged 필요
                    // 어댑터변수.notify~ 실행. 어댑터변수 X.
                    // 어댑터 내부에서는 직접 새로고침 가능 => runOnUiThread 필요
                    // runUiThread 대체재
                    Handler(Looper.getMainLooper()).post {
                        notifyDataSetChanged()
                    }
                }
            })
        }

        likeBtn.setOnClickListener(likeOrDislikeEvent)
        dislikeBtn.setOnClickListener(likeOrDislikeEvent)

//        완성된 row를 리스트뷰의 재료로 리턴
        return row

    }

}