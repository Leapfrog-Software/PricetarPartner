//
//  PostChatMessageRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import Foundation

class PostChatMessageRequester {
    
    class func post(targetId: String, message: String, completion: @escaping ((Bool) -> ())) {
        
        var params = ["command": "postChatMessage"]
        params["senderId"] = SaveData.shared.userId
        params["targetId"] = targetId
        params["message"] = message.base64Encode() ?? ""

        ApiRequester.post(params: params) { result, data in
            if result, let dic = data as? Dictionary<String, Any> {
                if (dic["result"] as? String) == "0" {
                    completion(true)
                    return
                }
            }
            completion(false)
        }
    }
}
