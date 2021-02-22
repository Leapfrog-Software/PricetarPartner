//
//  ProfileClientViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

class ProfileClientViewController: UIViewController {

    @IBOutlet private weak var nicknameTextField: UITextField!
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var useFreqencyLabel: UILabel!
    @IBOutlet private weak var conditionLabel: UILabel!
    @IBOutlet private weak var genreLabel: UILabel!
    @IBOutlet private weak var optionLabel: UILabel!
    @IBOutlet private weak var messageTextView: UITextView!
    @IBOutlet private weak var userImageView: UIImageView!
    
    private var selectedArea: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()

    }
    
    @IBAction func didEndOnExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapArea(_ sender: Any) {
        self.view.endEditing(true)
        
        let areaSelect = self.instantiate(storyboard: "MyPage", identifier: "ProfileAreaSelectViewController") as! ProfileAreaSelectViewController
        areaSelect.set(defaultArea: self.selectedArea, completion: { [weak self] area in
            self?.selectedArea = area
            self?.areaLabel.text = area
        })
        self.stack(viewController: areaSelect, animationType: .none)
    }
    
    @IBAction func onTapUseFrequency(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapCondition(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapGenre(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapOption(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapImage(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapRegister(_ sender: Any) {
        self.view.endEditing(true)
    }
}
