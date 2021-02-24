//
//  UserData.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

struct UserData {
    
    enum ProfileType: String {
        case none = "0"
        case client = "1"
        case partner = "2"
    }
        
    let id: String
    let nickname: String
    let area: String
    let profileType: ProfileType
    let clientUseFrequency: String
    let clientCondition: String
    let clientGenres: [String]
    let clientMessage: String
    let partnerCareer: String
    let partnerStatus: String
    let partnerNewPrice: String
    let partnerOldPrice: String
    let partnerDangerousPrice: String
    let partnerDangerousMessage: String
    let partnerInspectionPrice: String
    let partnerInspectionMessage: String
    let partnerMessage: String
    let loginDatetime: Date
    
    init?(data: Dictionary<String, Any>) {
     
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.nickname = (data["nickname"] as? String)?.base64Decode() ?? ""
        self.area = (data["area"] as? String)?.base64Decode() ?? ""
        
        guard let profileType = ProfileType(rawValue: data["profileType"] as? String ?? "") else {
            return nil
        }
        self.profileType = profileType
        
        self.clientUseFrequency = (data["clientUseFrequency"] as? String)?.base64Decode() ?? ""
        self.clientCondition = (data["clientCondition"] as? String)?.base64Decode() ?? ""
        self.clientGenres = (data["clientGenres"] as? String)?.components(separatedBy: ",").compactMap { $0.base64Decode() } ?? []
        self.clientMessage = (data["clientMessage"] as? String)?.base64Decode() ?? ""
        self.partnerCareer = (data["partnerCareer"] as? String)?.base64Decode() ?? ""
        self.partnerStatus = (data["partnerStatus"] as? String)?.base64Decode() ?? ""
        
        self.partnerNewPrice = data["partnerNewPrice"] as? String ?? ""
        self.partnerOldPrice = data["partnerOldPrice"] as? String ?? ""
        self.partnerDangerousPrice = data["partnerDangerousPrice"] as? String ?? ""
        self.partnerDangerousMessage = (data["partnerDangerousMessage"] as? String)?.base64Decode() ?? ""
        self.partnerInspectionPrice = data["partnerInspectionPrice"] as? String ?? ""
        self.partnerInspectionMessage = (data["partnerInspectionMessage"] as? String)?.base64Decode() ?? ""
        self.partnerMessage = (data["partnerMessage"] as? String)?.base64Decode() ?? ""
        
        guard let loginDatetimeStr = data["loginDatetime"] as? String, loginDatetimeStr.count == 14, let loginDatetime = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: loginDatetimeStr) else {
            return nil
        }
        self.loginDatetime = loginDatetime
    }
    
    static func imageUrl(userId: String) -> String {
        return Constants.ServerRootUrl + "data/user/" + userId + "/image"
    }
    
    func lastLoginString() -> String {
        
        let diff = Date().timeIntervalSince(self.loginDatetime)
        if diff < 60 {
            return "たった今"
        } else if diff < 60 * 60 {
            return "\(Int(floor(diff / 60)))分前"
        } else if diff < 24 * 60 * 60 {
            return "\(Int(floor(diff / 60 / 60)))時間前"
        } else if diff < 30 * 24 * 60 * 60 {
            return "\(Int(floor(diff / 60 / 60 / 24)))日前"
        } else {
            return "1ヶ月以上前"
        }
    }
}
