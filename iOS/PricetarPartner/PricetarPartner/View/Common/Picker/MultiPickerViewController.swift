//
//  MultiPickerViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

class MultiPickerViewController: UIViewController {
    
    @IBOutlet private weak var pickerTitleLabel: UILabel!
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var contentsTopConstraint: NSLayoutConstraint!
    @IBOutlet private weak var contentsHeaderHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var contentsHeightConstraint: NSLayoutConstraint!
    
    private var pickerTitle = ""
    fileprivate var dateArray = [String]()
    fileprivate var selectedIndexes = [Int]()
    fileprivate var completion: (([Int]) -> ())?
    
    class func show(on viewController: UIViewController, title: String, dataArray: [String], defaultIndexes: [Int], completion: (([Int]) -> ())?) {
        
        let picker = viewController.instantiate(storyboard: "Common", identifier: "MultiPickerViewController") as! MultiPickerViewController
        picker.pickerTitle = title
        picker.dateArray = dataArray
        picker.selectedIndexes = defaultIndexes
        picker.completion = completion
        viewController.stack(viewController: picker, animationType: .none)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.contentsTopConstraint.constant = UIScreen.main.bounds.size.height
        
        var height = self.contentsHeaderHeightConstraint.constant + CGFloat(self.dateArray.count) * self.tableView.rowHeight
        if height > UIScreen.main.bounds.size.height - PickerViewController.Const.topMargin {
            height = UIScreen.main.bounds.size.height - PickerViewController.Const.topMargin
        }
        self.contentsHeightConstraint.constant = height - self.contentsHeaderHeightConstraint.constant
        
        self.pickerTitleLabel.text = self.pickerTitle
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        let topConstraint = UIScreen.main.bounds.size.height - self.contentsHeaderHeightConstraint.constant - self.contentsHeightConstraint.constant
        self.contentsTopConstraint.constant = topConstraint
        
        UIView.animate(withDuration: 0.2) { [weak self] in
            self?.view.layoutIfNeeded()
        }
    }
    
    @IBAction func onTapOk(_ sender: Any) {
        self.completion?(self.selectedIndexes)
        self.close()
    }
    
    @IBAction func onTapCancel(_ sender: Any) {
        self.close()
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.close()
    }
    
    fileprivate func close() {
        
        self.contentsTopConstraint.constant = UIScreen.main.bounds.size.height
        UIView.animate(withDuration: 0.2, animations: { [weak self] in
            self?.view.layoutIfNeeded()
        }) { [weak self] _ in
            self?.pop(animationType: .none)
        }
    }
}

extension MultiPickerViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.dateArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PickerTableViewCell", for: indexPath) as! PickerTableViewCell
        cell.configure(title: self.dateArray[indexPath.row], isSelected: self.selectedIndexes.contains(indexPath.row))
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if let index = self.selectedIndexes.firstIndex(of: indexPath.row) {
            self.selectedIndexes.remove(at: index)
        } else {
            self.selectedIndexes.append(indexPath.row)
        }
        tableView.reloadData()
    }
}
