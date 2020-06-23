package com.jisu.apipractice_20200615.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.jisu.apipractice_20200615.R
import com.jisu.apipractice_20200615.ViewReplyDetailActivity
import com.jisu.apipractice_20200615.datas.TopicReply
import com.jisu.apipractice_20200615.utils.ServerUtil
import org.json.JSONObject
import java.text.SimpleDateFormat

class TopicReReplyAdapter(
    val mContext:Context,
    val resId:Int,
    val mList:List<TopicReply>) : ArrayAdapter<TopicReply>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        tempRow?.let {

        }.let {
            tempRow = inf.inflate(R.layout.topic_rereply_list_item, null)
        }

//        row가 절대 null 아님을 보장하면서 대입
        val row = tempRow!!

        val replyNickNameTxt = row.findViewById<TextView>(R.id.replyNickNameTxt)
        val replyContentTxt = row.findViewById<TextView>(R.id.replyContentTxt)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val dislikeBtn = row.findViewById<Button>(R.id.dislikeBtn)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)

        val data = mList[position]

        replyNickNameTxt.text = data.writer.nickname
        replyContentTxt.text = data.content

        //  선택 진영 정보 반영
        selectedSideTitleTxt.text = "(${data.selectedSide.title})"

        // 좋아요 / 싫어요 / 답글 갯수 표시
        likeBtn.text = "좋아요 : ${data.likeCount.toString()}개"
        dislikeBtn.text = "싫어요 : ${data.dislikeCount.toString()}개"
        
        // 내 좋아요 여부 / 싫어요 여부 표시
        // 내 좋아요 : 좋아요 빨강 / 싫어요 회색
        // 내 싫어요 : 좋아요 회색 / 싫어요 파랑
        // 그 외 (둘다 안 찍은 경우) 좋아요 회색 / 싫어요 회색
        // 글씨 색도 같은 색으로 설정
        if (data.isMyLike) {
            likeBtn.setBackgroundResource(R.drawable.red_border_box)
            dislikeBtn.setBackgroundResource(R.drawable.gray_border_box)

            likeBtn.setTextColor(mContext.resources.getColor(R.color.red))
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.deepGray))
        } else if (data.isMyDislike) {
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
            dislikeBtn.setBackgroundResource(R.drawable.blue_border_box)

            likeBtn.setTextColor(mContext.resources.getColor(R.color.deepGray))
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.indigo))
        } else {
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
            dislikeBtn.setBackgroundResource(R.drawable.gray_border_box)

            likeBtn.setTextColor(mContext.resources.getColor(R.color.deepGray))
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.deepGray))
        }

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

                    // data 내부 값중 좋아요/싫어요 실시간 반영 처리
                    data.isMyLike = reply.getBoolean("my_like")
                    data.isMyDislike = reply.getBoolean("my_dislike")

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