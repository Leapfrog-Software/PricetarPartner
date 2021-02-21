//
//  FetchUserRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

class FetchUserRequester {
    
    static let shared = FetchUserRequester()
    
    var dataList = [UserData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        var params = ["command": "getUser"]
        params["userId"] = SaveData.shared.userId
        
        ApiRequester.post(params: params) { [weak self] result, data in
            if result, let dic = data as? Dictionary<String, Any> {
                if (dic["result"] as? String) == "0", let users = (dic["users"] as? Array<Dictionary<String, Any>>) {
                    self?.dataList = users.compactMap { UserData(data: $0) }
                    completion(true)
                    return
                }
            }
            completion(false)
        }
    }
    
    func query(userId: String) -> UserData? {
        return self.dataList.first { $0.id == userId }
    }
}
