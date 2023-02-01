package com.baseapp.main.lyrics

class LineData {
    //是否允许绘制拼音
    var isDrawPinyin = true

    //行间距
    var lineSpacing: Float = 0f

    //是否有拼音
    var hasPinyin = false

    //行高
    var lineHeight: Float = 0f

    //行宽
    var allTextWidth: Float = 0f

    //拼音高
    var pinyinHeight: Float = 0f

    //文字高
    var textHeight: Float = 0f

    //文字数据
    var textList = arrayListOf<TextData>()

    /**
     * 添加文字数据，同时更新行高数据
     */
    fun putText(textData: TextData) {
        allTextWidth += textData.width

        textList.add(textData)

        if (!textData.pinyin.isNullOrEmpty()) {
            hasPinyin = true
        }

        lineHeight = if (isDrawPinyin && hasPinyin) {
            pinyinHeight + textHeight + lineSpacing
        } else {
            textHeight + lineSpacing
        }
    }

    data class TextData(
        val word: String,
        val pinyin: String?,
        val width: Float,
        val pos: Int //该字在数据结构中的位置
    )
}