//
//  MessageViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class MessageViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var chatGroups = [ChatGroupData]()

    func reload() {
        
        self.chatGroups = FetchChatGroupRequester.shared.dataList
        self.tableView.reloadData()

        if self.chatGroups.count > 0 {
            self.tableView.isHidden = false
            self.noDataLabel.isHidden = true
        } else {
            self.tableView.isHidden = true
            self.noDataLabel.isHidden = false
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.reload()
    }
}

extension MessageViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.chatGroups.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageTableViewCell", for: indexPath) as! MessageTableViewCell
        cell.configure(chatGroupData: self.chatGroups[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let chatGroup = self.chatGroups[indexPath.row]
        let targetId = (chatGroup.userId1 == SaveData.shared.userId) ? chatGroup.userId2 : chatGroup.userId1
        
        let chat = self.instantiate(storyboard: "Message", identifier: "ChatViewController") as! ChatViewController
        chat.set(targetId: targetId)
        self.getTabbarViewController()?.stack(viewController: chat, animationType: .horizontal)
    }
}
