//
//  ChatData.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import Foundation

struct ChatData {
    
    enum ChatType: String {
        case message = "0"
        case image = "1"
    }
        
    let id: String
    let groupId: String
    let senderId: String
    let targetId: String
    let datetime: Date
    let type: ChatType
    let message: String
    
    init?(data: Dictionary<String, Any>) {

        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.groupId = data["groupId"] as? String ?? ""
        self.senderId = data["senderId"] as? String ?? ""
        self.targetId = data["targetId"] as? String ?? ""
        
        guard let datetimeStr = data["datetime"] as? String, let datetime = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: datetimeStr) else {
            return nil
        }
        self.datetime = datetime
        
        guard let chatType = ChatType(rawValue: data["type"] as? String ?? "") else {
            return nil
        }
        self.type = chatType

        self.message = (data["message"] as? String)?.base64Decode() ?? ""
    }
    
    func imageUrl() -> String {
        return Constants.ServerRootUrl + "data/chat/" + self.groupId + "/" + self.id
    }
    
    func getDisplayDatetime() -> String {
        
        let dateStr = DateFormatter(dateFormat: "yyyy/M/d").string(from: self.datetime)
        let hour = Int(DateFormatter(dateFormat: "HH").string(from: self.datetime)) ?? 0
        let minute = DateFormatter(dateFormat: "mm").string(from: self.datetime)
        let timeStr = (hour < 12) ? ("\(hour):" + minute + " AM") : ("\(hour - 12):" + minute + " PM")
        return dateStr + ", " + timeStr
    }
}
