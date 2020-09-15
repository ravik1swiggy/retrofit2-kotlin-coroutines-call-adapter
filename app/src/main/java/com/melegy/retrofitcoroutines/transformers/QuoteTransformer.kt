package com.melegy.retrofitcoroutines.transformers

import com.melegy.retrofitcoroutines.BaseResponse
import com.melegy.retrofitcoroutines.remote.vo.Response
import com.melegy.retrofitcoroutines.room.Quote
import com.melegy.retrofitcoroutines.room.QuoteResponse
import com.melegy.retrofitcoroutines.room.toQuote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class QuoteTransformer : FlowModelTransformer<BaseResponse<QuoteResponse>, Quote>() {

	override fun execute(parameters: BaseResponse<QuoteResponse>): Flow<Response<Quote>> {
		return flowOf(Response.success(parameters.data?.toQuote()))
	}
}
