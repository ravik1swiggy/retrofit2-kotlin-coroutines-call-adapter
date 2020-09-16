package com.melegy.retrofitcoroutines

data class RandomResponse(val dum1: String = "ravi", val dum2: String = "tejaaskdfjasdfj alsdjflkajsdlkf jasldkf jlaksdjflkajsdlfk ajlsdkjflka sdjf lkasdjflkas djflkajsdlkfj alksdjf lkasdjflkasdjflkajs dlkfj alsdjflaks djlf jalsdkfj lasdkjf lkasdj", val dum3: Int = 4)

val randomResponse = RandomResponse()

val responseHash  by lazy { randomResponse.toString().toMD5() }
