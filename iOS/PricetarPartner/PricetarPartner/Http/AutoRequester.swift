//
//  AutoRequester.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/08.
//

import Foundation

class AutoRequester {
    
    private var timer: Timer?
    private var lastFetchDatetime: Date?
    private var didFetch: (() -> ())?
    
    func start(didFetch: (() -> ())?) {
        
        self.lastFetchDatetime = nil
        self.timer?.invalidate()

        self.didFetch = didFetch
        
        self.timer = Timer.scheduledTimer(withTimeInterval: 1.0, repeats: true, block: { [weak self] _ in
            self?.timerProc()
        })
    }
    
    func restart() {
        self.lastFetchDatetime = nil
    }
    
    func stop() {
        self.timer?.invalidate()
    }
    
    private func timerProc() {
        
        if let lastFetchDatetime = self.lastFetchDatetime {
            if Date().timeIntervalSince(lastFetchDatetime) > 60 {
                self.fetch()
            }
        } else {
            self.fetch()
        }
    }
    
    private func fetch() {
        
        FetchUserRequester.shared.fetch(completion: { resultUser in
            FetchChatGroupRequester.shared.fetch(completion: { resultChatGroup in
                if resultUser && resultChatGroup {
                    self.didFetch?()
                }
            })
        })
        self.lastFetchDatetime = Date()
    }
}
