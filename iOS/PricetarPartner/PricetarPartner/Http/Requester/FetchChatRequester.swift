//
//  FetchChatRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import Foundation

class FetchChatRequester {
    
    class func fetch(targetId: String, completion: @escaping ((Bool, [ChatData]?) -> ())) {
        
        var params = ["command": "getChat"]
        params["userId1"] = SaveData.shared.userId
        params["userId2"] = targetId
        
        ApiRequester.post(params: params) { result, data in
            if result, let dic = data as? Dictionary<String, Any> {
                if (dic["result"] as? String) == "0", let chats = (dic["chats"] as? Array<Dictionary<String, Any>>) {
                    let chatDatas = chats.compactMap { ChatData(data: $0) }
                    completion(true, chatDatas)
                    return
                }
            }
            completion(false, nil)
        }
    }
}
