//
//  ChatViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/03/02.
//

import UIKit

class ChatViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var messageTextView: UITextView!
    @IBOutlet private weak var messageTextViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var messagePlaceHolderLabel: UILabel!
    
    private var targetId: String!
    private var chats = [ChatData]()
    private var timer: Timer?
    private var initialReload = true
    private var dummyLeftMessageCell: ChatMessageLeftTableViewCell?
    private var dummyRightMessageCell: ChatMessageRightTableViewCell?
    private var dummyMessageTextView: UITextView?
    private let imagePicker = ImagePickerManager()
    
    func set(targetId: String) {
        self.targetId = targetId
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.dummyLeftMessageCell = self.tableView.dequeueReusableCell(withIdentifier: "ChatMessageLeftTableViewCell") as? ChatMessageLeftTableViewCell
        self.dummyLeftMessageCell?.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 1)
        self.dummyRightMessageCell = self.tableView.dequeueReusableCell(withIdentifier: "ChatMessageRightTableViewCell") as? ChatMessageRightTableViewCell
        self.dummyRightMessageCell?.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: 1)
        
        self.messageTextView.textContainerInset = UIEdgeInsets(top: 15, left: 0, bottom: 15, right: 0)
        self.messageTextView.textContainer.lineFragmentPadding = 0
        
        Loading.start()
        
        self.timer = Timer.scheduledTimer(withTimeInterval: 5.0, repeats: true, block: { [weak self] _ in
            self?.timerProc()
        })
        self.timerProc()
    }
    
    private func timerProc() {
        
        FetchChatRequester.fetch(targetId: self.targetId, completion: { result, chats in
            Loading.stop()
            
            if result, let chats = chats {
                if chats.count == self.chats.count {
                    return
                }
                self.chats = chats
                self.tableView.reloadData()
                
                if self.initialReload {
                    self.initialReload = false
                    self.scrollToBottom()
                } else {
                    self.scrollToBottomIfNeeded()
                }
            }
        })
    }
    
    private func scrollToBottomIfNeeded() {
        
        if self.tableView.contentOffset.y + self.tableView.frame.size.height + 100 > self.tableView.contentSize.height {
            self.scrollToBottom()
        }
    }
    
    private func scrollToBottom() {
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            if self.tableView.contentSize.height > self.tableView.frame.size.height {
                let offset = CGPoint(x: 0, y: self.tableView.contentSize.height - self.tableView.frame.size.height)
                self.tableView.setContentOffset(offset, animated: true)
            }
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        self.view.endEditing(true)
        self.timer?.invalidate()
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapSend(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let message = self.messageTextView.text ?? ""
        if message.count == 0 {
            return
        }
        
        PostChatMessageRequester.post(targetId: self.targetId, message: message, completion: { result in
            if result {
                self.timerProc()
            } else {
                Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
        
        self.messageTextView.text = ""
        self.messageTextViewHeightConstraint.constant = 50
    }
    
    @IBAction func onTapCamera(_ sender: Any) {
        
        self.view.endEditing(true)
        
        self.imagePicker.showPicker(on: self, type: .photoLibrary, completion: { [weak self] image in
            if let chatImage = image.toChatImage(), let targetId = self?.targetId {
                PostChatImageRequester.post(targetId: targetId, image: chatImage, completion: { result in
                    if result {
                        self?.timerProc()
                    } else {
                        Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
                    }
                })
            }
        })
    }
}

extension ChatViewController: UITextViewDelegate {
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        
        if self.dummyMessageTextView == nil {
            self.dummyMessageTextView = UITextView()
            self.dummyMessageTextView?.font = textView.font
            self.dummyMessageTextView?.frame = textView.frame
            self.dummyMessageTextView?.textContainerInset = textView.textContainerInset
            self.dummyMessageTextView?.textContainer.lineFragmentPadding = textView.textContainer.lineFragmentPadding
        }

        let dummyText = NSMutableString(string: textView.text)
        dummyText.deleteCharacters(in: range)
        dummyText.insert(text, at: range.location)
        self.dummyMessageTextView?.text = dummyText as String
        
        var size = self.dummyMessageTextView?.sizeThatFits(textView.frame.size) ?? CGSize.zero
        if size.height < 50 {
            size.height = 50
        }
        if size.height > 120 {
            size.height = 120
        }
        self.messageTextViewHeightConstraint.constant = size.height
        
        self.messagePlaceHolderLabel.isHidden = dummyText.length > 0
        
        return true
    }
}

extension ChatViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.chats.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let chatData = self.chats[indexPath.row]
        
        if chatData.type == .message {
            if chatData.senderId == SaveData.shared.userId {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ChatMessageRightTableViewCell", for: indexPath) as! ChatMessageRightTableViewCell
                cell.configure(chatData: chatData)
                return cell
            } else {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ChatMessageLeftTableViewCell", for: indexPath) as! ChatMessageLeftTableViewCell
                cell.configure(chatData: chatData)
                return cell
            }
        } else {
            if chatData.senderId == SaveData.shared.userId {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ChatImageRightTableViewCell", for: indexPath) as! ChatImageRightTableViewCell
                cell.configure(chatData: chatData)
                return cell
            } else {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ChatImageLeftTableViewCell", for: indexPath) as! ChatImageLeftTableViewCell
                cell.configure(chatData: chatData)
                return cell
            }
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        let chatData = self.chats[indexPath.row]
        
        if chatData.type == .message {
            if chatData.senderId == SaveData.shared.userId {
                return self.dummyRightMessageCell?.height(chatData: self.chats[indexPath.row]) ?? 0
            } else {
                return self.dummyLeftMessageCell?.height(chatData: self.chats[indexPath.row]) ?? 0
            }
        } else {
            return 210
        }
    }
}
