//
//  PostChatImageRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import UIKit

class PostChatImageRequester {
    
    class func post(targetId: String, image: UIImage, completion: @escaping ((Bool) -> ())) {
        
        var params = ["command": "postChatImage"]
        params["senderId"] = SaveData.shared.userId
        params["targetId"] = targetId
        params["image"] = image.pngData()?.base64EncodedString() ?? ""

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
