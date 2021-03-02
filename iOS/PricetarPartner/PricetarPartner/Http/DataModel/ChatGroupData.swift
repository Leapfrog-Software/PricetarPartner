//
//  ChatGroupData.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import Foundation

struct ChatGroupData {
    
    let id: String
    let userId1: String
    let userId2: String
    let unreadCount1: Int
    let unreadCount2: Int
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.userId1 = data["userId1"] as? String ?? ""
        self.userId2 = data["userId2"] as? String ?? ""
        self.unreadCount1 = Int(data["unreadCount1"] as? String ?? "") ?? 0
        self.unreadCount2 = Int(data["unreadCount2"] as? String ?? "") ?? 0
    }
}
