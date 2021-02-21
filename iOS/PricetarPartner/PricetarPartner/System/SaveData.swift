//
//  SaveData.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import Foundation

struct UserDefaultsKey {
    static let userId = "userId"
    static let fcmToken = "fcmToken"
}

class SaveData {
    
    static let shared = SaveData()
    
    var userId = ""
    var fcmToken = ""
    
    init() {
        
        let userDefaults = UserDefaults()
        
        self.userId = userDefaults.string(forKey: UserDefaultsKey.userId) ?? ""
        self.fcmToken = userDefaults.string(forKey: UserDefaultsKey.fcmToken) ?? ""
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        
        userDefaults.set(self.userId, forKey: UserDefaultsKey.userId)
        userDefaults.set(self.fcmToken, forKey: UserDefaultsKey.fcmToken)

        userDefaults.synchronize()
    }
}
