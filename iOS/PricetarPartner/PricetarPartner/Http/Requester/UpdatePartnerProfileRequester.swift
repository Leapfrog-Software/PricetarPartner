//
//  UpdatePartnerProfileRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/23.
//

import UIKit

class UpdatePartnerProfileRequester {
        
    class func update(nickname: String, area: String, career: String, status: String, newPrice: String, oldPrice: String, dangerousPrice: String, dangerousMessage: String, bigPrice: String, bigMessage: String, inspectionPrice: String, inspectionMessage: String, message: String, image: UIImage?, completion: @escaping ((Bool) -> ())) {
        
        var params = ["command": "updatePartnerProfile"]
        params["userId"] = SaveData.shared.userId
        params["nickname"] = nickname.base64Encode() ?? ""
        params["area"] = area.base64Encode() ?? ""
        params["career"] = career.base64Encode() ?? ""
        params["status"] = status.base64Encode() ?? ""
        params["newPrice"] = newPrice.base64Encode() ?? ""
        params["oldPrice"] = oldPrice.base64Encode() ?? ""
        params["dangerousPrice"] = dangerousPrice.base64Encode() ?? ""
        params["dangerousMessage"] = dangerousMessage.base64Encode() ?? ""
        params["bigPrice"] = bigPrice.base64Encode() ?? ""
        params["bigMessage"] = bigMessage.base64Encode() ?? ""
        params["inspectionPrice"] = inspectionPrice.base64Encode() ?? ""
        params["inspectionMessage"] = inspectionMessage.base64Encode() ?? ""
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
