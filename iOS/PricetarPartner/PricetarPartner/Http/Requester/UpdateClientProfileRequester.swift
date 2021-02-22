//
//  UpdateClientProfileRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

class UpdateClientProfileRequester {
        
    class func update(nickname: String, area: String, useFrequency: String, condition: String, genres: [String], options: [String], message: String, image: UIImage?, completion: @escaping ((Bool) -> ())) {
        
        var params = ["command": "updateClientProfile"]
        params["userId"] = SaveData.shared.userId
        params["nickname"] = nickname.base64Encode() ?? ""
        params["useFrequency"] = useFrequency.base64Encode() ?? ""
        params["condition"] = condition.base64Encode() ?? ""
        params["genres"] = genres.compactMap { $0.base64Encode() }.joined(separator: ",")
        params["options"] = options.compactMap { $0.base64Encode() }.joined(separator: ",")
        params["message"] = message.base64Encode() ?? ""
        
        if let image = image {
            params["image"] = image.pngData()?.base64EncodedString() ?? ""
        } else {
            params["image"] = ""
        }
        
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
