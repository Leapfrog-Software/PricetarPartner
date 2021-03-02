//
//  FetchChatGroupRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import Foundation

class FetchChatGroupRequester {
    
    static let shared = FetchChatGroupRequester()
    
    var dataList = [ChatGroupData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        var params = ["command": "getChatGroup"]
        params["userId"] = SaveData.shared.userId
        
        ApiRequester.post(params: params) { [weak self] result, data in
            if result, let dic = data as? Dictionary<String, Any> {
                if (dic["result"] as? String) == "0", let users = (dic["chatGroups"] as? Array<Dictionary<String, Any>>) {
                    self?.dataList = users.compactMap { ChatGroupData(data: $0) }
                    completion(true)
                    return
                }
            }
            completion(false)
        }
    }
}
